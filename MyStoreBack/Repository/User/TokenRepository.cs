using Microsoft.AspNetCore.Identity;
using MyStoreBack.Data.Context;
using MyStoreBack.Data.Entity.Identity;
using MyStoreBack.Models.Identity;

namespace MyStoreBack.Repository.User;

public class TokenRepository : ITokenRepository
{
    private readonly UserManager<IdentityUser> _userManager;
    private readonly StoreDbContext _db;

    public TokenRepository(UserManager<IdentityUser> userManager, StoreDbContext db)
    {
        _userManager = userManager;
        _db = db;
    }

    public async Task<UserRefreshTokens> AddUserRefreshTokens(UserRefreshTokens user)
    {
        await _db.RefreshTokens.AddAsync(user);
        await _db.SaveChangesAsync();
        return user;
    }

    public async Task DeleteUserRefreshTokens(string username, string refreshToken)
    {
        var item = _db.RefreshTokens.FirstOrDefault(x => 
            x.UserName == username && 
            x.RefreshToken == refreshToken);

        if (item is null) return;
        _db.RefreshTokens.Remove(item);
        await _db.SaveChangesAsync();
    }
	
    public UserRefreshTokens? GetSavedRefreshTokens(string username, string refreshToken)
    {
        return _db.RefreshTokens.FirstOrDefault(x => 
            x.UserName == username && 
            x.RefreshToken == refreshToken && 
            x.IsActive);
    }
	
    public async Task<bool> IsValidUserAsync(LoginModel user)
    {
        var u = _userManager.Users.FirstOrDefault(o => o.UserName == user.Email);
        return u is not null 
               && await _userManager.CheckPasswordAsync(u, user.Password);
    }
}