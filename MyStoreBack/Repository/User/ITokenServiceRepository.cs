using MyStoreBack.Data.Entity.Identity;
using MyStoreBack.Models.Identity;

namespace MyStoreBack.Repository.User;

public interface ITokenServiceRepository
{
    Task<bool> IsValidUserAsync(LoginModel user);
	
    Task<UserRefreshTokens> AddUserRefreshTokens(UserRefreshTokens tokens);

    UserRefreshTokens? GetSavedRefreshTokens(string username, string refreshToken);

    Task DeleteUserRefreshTokens(string username, string refreshToken);
}