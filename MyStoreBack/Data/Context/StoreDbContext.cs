using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using MyStoreBack.Data.Entity;
using MyStoreBack.Data.Entity.Identity;

namespace MyStoreBack.Data.Context;

public class StoreDbContext : IdentityDbContext<UserEntity, RoleEntity, int,
    IdentityUserClaim<int>, UserRoleEntity, IdentityUserLogin<int>,
    IdentityRoleClaim<int>, IdentityUserToken<int>>
{
    public StoreDbContext(DbContextOptions<StoreDbContext> opts)
        : base(opts) {}

    public DbSet<CategoryEntity> Categories { get; set; } = null!;
    public DbSet<UserRefreshTokens> RefreshTokens { get; set; } = null!;
    
    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder);
        builder.Entity<UserRoleEntity>(ur =>
        {
            ur.HasKey(entity => new { entity.UserId, entity.RoleId });
            ur.HasOne(entity => entity.Role)
                .WithMany(r => r.UserRoles)
                .HasForeignKey(r => r.RoleId)
                .IsRequired();

            ur.HasOne(entity => entity.User)
                .WithMany(r => r.UserRoles)
                .HasForeignKey(u => u.UserId)
                .IsRequired();
        });
    }
}