using MyStoreBack.Data.Entity.Identity;

namespace MyStoreBack.Security;

public interface IJwtTokenService
{
    Task<string> CreateTokenAsync(UserEntity user);
    string CreateRefreshToken();
}