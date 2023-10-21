using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using MyStoreBack.Data.Entity.Identity;
using JwtRegisteredClaimNames = Microsoft.IdentityModel.JsonWebTokens.JwtRegisteredClaimNames;

namespace MyStoreBack.Security;

public class JwtTokenServiceImpl : IJwtTokenService
{
    private IConfiguration Configuration { get; }
    private UserManager<UserEntity> UserManager { get; }

    public JwtTokenServiceImpl(
        IConfiguration configuration,
        UserManager<UserEntity> userManager)
    {
        Configuration = configuration;
        UserManager = userManager;
    }
    
    public async Task<string> CreateTokenAsync(UserEntity user)
    {
        var claims = new List<Claim>
        {
            new(ClaimTypes.NameIdentifier, user.Id.ToString()),
            new(ClaimTypes.Name, user.UserName),
            new(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
            new("image", user.Image ?? "user.jpg"),
        };
        
        var roles = await UserManager.GetRolesAsync(user);
        foreach (var role in roles)
            claims.Add(new Claim(ClaimTypes.Role, role));
        
        string secretKey = Configuration["JwtSecretKey"];
        SymmetricSecurityKey key = new(Encoding.UTF8.GetBytes(secretKey));
        SigningCredentials credentials = new(key, SecurityAlgorithms.HmacSha256);
        
        JwtSecurityToken jwt = new(
            signingCredentials: credentials,
            claims: claims,
            expires: DateTime.Now.AddDays(15)
        );
        
        return new JwtSecurityTokenHandler().WriteToken(jwt);
    }

    public string CreateRefreshToken()
    {
        var randomNumber = new byte[32];
        using var rng = RandomNumberGenerator.Create();
        rng.GetBytes(randomNumber);
        return Convert.ToBase64String(randomNumber);
    }
}