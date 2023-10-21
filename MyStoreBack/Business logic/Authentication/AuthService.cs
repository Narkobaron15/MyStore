using AutoMapper;
using Microsoft.AspNetCore.Identity;
using MyStoreBack.Constants;
using MyStoreBack.Data.Entity.Identity;
using MyStoreBack.Models.Identity;
using MyStoreBack.Security;

namespace MyStoreBack.Business_logic.Authentication;

public class AuthService : IAuthService
{
    private readonly IJwtTokenService _jwtTokenService;
    private readonly UserManager<UserEntity> _userManager;
    private readonly IMapper _mapper;

    public AuthService(
        IJwtTokenService jwtTokenService, 
        UserManager<UserEntity> userManager, 
        IMapper mapper)
    {
        _jwtTokenService = jwtTokenService;
        _userManager = userManager;
        _mapper = mapper;
    }

    private static readonly string WrongEmailOrPwdMsg 
        = "Email or password is incorrect.";
    
    public async Task<Tokens> Login(LoginModel model)
    {
        UserEntity user = 
            await _userManager.FindByEmailAsync(model.Email);
        if (user is null) throw new InvalidDataException(WrongEmailOrPwdMsg);

        bool isPasswordValid = 
            await _userManager.CheckPasswordAsync(user, model.Password);
        if (!isPasswordValid) throw new InvalidDataException(WrongEmailOrPwdMsg);

        string token = await _jwtTokenService.CreateTokenAsync(user);
        return new Tokens{ AccessToken = token };
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

    public async Task<Tokens> Refresh(Tokens model)
    {
        // Stub
        await Task.FromException(new NotImplementedException());
        return null!;
    }
}