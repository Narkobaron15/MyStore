using System.Security.Claims;
using MyStoreBack.Data.Entity.Identity;
using MyStoreBack.Models.Identity;

namespace MyStoreBack.Repository.Security;

public interface IJwtTokenService
{
    Task<TokensModel> GenerateToken(UserEntity user);
    ClaimsPrincipal GetPrincipalFromExpiredToken(string token);
}