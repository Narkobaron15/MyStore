using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using MyStoreBack.Data.Entity.Base;

namespace MyStoreBack.Data.Entity;

[Table("tbl_categories")]
public class CategoryEntity : BaseEntity<int>
{
    public CategoryEntity()
        :this(string.Empty, string.Empty) { }
    
    public CategoryEntity(string name, string imageUrl, string? description = null)
    {
        Name = name;
        ImageUrl = imageUrl;
        Description = description ?? string.Empty;
        CreatedAt = DateTime.UtcNow;
    }
    
    [Required, StringLength(256)]
    public string Name { get; set; }

    [Required, StringLength(256)]
    public string ImageUrl { get; set; }
    
    [StringLength(4000)]
    public string Description { get; set; }
}