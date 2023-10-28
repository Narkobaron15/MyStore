﻿using AutoMapper;
using Microsoft.AspNetCore.Identity;
using MyStoreBack.Constants;
using MyStoreBack.Data.Entity.Identity;
using MyStoreBack.Models.Identity;
using MyStoreBack.Repository.Security;
using MyStoreBack.Repository.User;

namespace MyStoreBack.Business_logic.Authentication;

public class AuthService : IAuthService
{
    private readonly IJwtTokenService _tokenService;
    private readonly ITokenRepository _tokenRepository;
    private readonly UserManager<UserEntity> _userManager;
    private readonly IMapper _mapper;

    public AuthService(
        IJwtTokenService tokenService, 
        ITokenRepository tokenRepository,
        UserManager<UserEntity> userManager, 
        IMapper mapper)
    {
        _tokenService = tokenService;
        _tokenRepository = tokenRepository;
        _userManager = userManager;
        _mapper = mapper;
    }

    private static readonly string WrongEmailOrPwdMsg 
        = "Email or password is incorrect.";
    
    public async Task<TokensModel> Login(LoginModel model)
    {
        UserEntity user = await _userManager.FindByEmailAsync(model.Email);
        if (user is null) 
            throw new InvalidDataException(WrongEmailOrPwdMsg);

        bool isPasswordValid = 
            await _userManager.CheckPasswordAsync(user, model.Password);
        if (!isPasswordValid) 
            throw new InvalidDataException(WrongEmailOrPwdMsg);

        var tokens = await _tokenService.GenerateToken(user);
        await _tokenRepository.AddUserRefreshTokens(new UserRefreshTokens
        {
            UserName = user.UserName,
            RefreshToken = tokens.RefreshToken,
        });
        return tokens;
    }

    public async Task Register(RegisterModel model)
    {
        var userEntity = _mapper.Map<UserEntity>(model);
        var result = await _userManager.CreateAsync(userEntity, model.Password);
        await _userManager.AddToRoleAsync(userEntity, Roles.User);

        if (result.Errors.Any())
        {
            var errors = result.Errors.Select(e => e.Description);
            throw new InvalidDataException(string.Join('\n', errors));
        }
    }

    public async Task<TokensModel> Refresh(TokensModel model)
    {
        var principal = _tokenService.GetPrincipalFromExpiredToken(model.AccessToken);
        var user = await _userManager.GetUserAsync(principal);
        var username = user.UserName;

        //retrieve the saved refresh token from database
        if (username != null)
        {
            var savedRefreshToken = _tokenRepository.GetSavedRefreshTokens(username, model.RefreshToken);

            if (savedRefreshToken != null && savedRefreshToken.RefreshToken != model.RefreshToken)
            {
                return Unauthorized("Invalid attempt!");
            }
        }

        var newJwtToken = _tokenService.GenerateToken(user);

        if (newJwtToken == null)
        {
            return Unauthorized("Invalid attempt!");
        }

        // saving refresh token to the db
        UserRefreshTokens obj = new UserRefreshTokens
        {
            RefreshToken = newJwtToken.Refresh_Token,
            UserName = username
        };

        await _tokenRepository.DeleteUserRefreshTokens(username, model.RefreshToken);
        await _tokenRepository.AddUserRefreshTokens(obj);
        
    }
}