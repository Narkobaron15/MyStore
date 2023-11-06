﻿using System.ComponentModel.DataAnnotations;

namespace MyStoreBack.Models.Category;

public class CategoryUpdateModel
{
    public int Id { get; set; }
    
    [StringLength(100), Required]
    public string Name { get; set; } = string.Empty;
    
    public IFormFile? Image { get; set; }

    [StringLength(500), Required]
    public string Description { get; set; } = string.Empty;
}