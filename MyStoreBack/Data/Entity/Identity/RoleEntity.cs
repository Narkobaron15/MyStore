using Microsoft.AspNetCore.Identity;

namespace MyStoreBack.Data.Entity.Identity;

public class RoleEntity : IdentityRole<int>
{
    public virtual ICollection<UserRoleEntity> UserRoles { get; set; } = new List<UserRoleEntity>();
}