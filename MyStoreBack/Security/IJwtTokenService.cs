using MyStoreBack.Data.Entity.Identity;

namespace MyStoreBack.Security;

public interface IJwtTokenService
{
    Task<string> CreateAccessToken(UserEntity user);
    string CreateRefreshToken();
}