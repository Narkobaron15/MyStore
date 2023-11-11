using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using MyStoreBack.Data.Entity.Base;
using MyStoreBack.Data.Entity.Identity;

namespace MyStoreBack.Data.Entity;

[Table("tbl_categories")]
public class CategoryEntity : BaseEntity<int>
{
    [Required, StringLength(256)]
    public string Name { get; set; } = String.Empty;

    [Required, StringLength(256)]
    public string ImageUrl { get; set; } = String.Empty;
    
    [StringLength(4000)]
    public string Description { get; set; } = String.Empty;
    
    [ForeignKey("User")] 
    public int UserId { get; set; }
    
    public virtual UserEntity User { get; set; } = null!;
}