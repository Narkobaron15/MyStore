using Microsoft.AspNetCore.Identity;

namespace MyStoreBack.Data.Entity.Identity;

public class UserRoleEntity : IdentityUserRole<int>
{
    public UserEntity User { get; set; } = null!;
    public RoleEntity Role { get; set; } = null!;
}