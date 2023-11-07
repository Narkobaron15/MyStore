using AutoMapper;
using Microsoft.EntityFrameworkCore;
using MyStoreBack.Business_logic.Files;
using MyStoreBack.Data.Context;
using MyStoreBack.Data.Entity;
using MyStoreBack.Models.Category;

namespace MyStoreBack.Business_logic.Category;

public class CategoryService : ICategoryService
{
    private readonly IPictureService _pictureService;
    private StoreDbContext Context { get; }
    private IMapper Mapper { get; }

    public CategoryService(
        StoreDbContext context,
        IMapper mapper,
        IPictureService pictureService
    )
    {
        _pictureService = pictureService;
        Context = context;
        Mapper = mapper;
    }
    
    public async Task<IEnumerable<CategoryModel>> GetAll()
    {
        var categories = await Context.Categories.ToListAsync();
        var mapped = Mapper.Map<IEnumerable<CategoryModel>>(categories);

        return mapped;
    }

    public async Task<CategoryModel> GetById(int id)
    {
        var cat = await Context.Categories.FindAsync(id);
        var mapped = Mapper.Map<CategoryModel>(cat);
        
        return mapped;
    }

    public async Task<CategoryModel?> Create(CategoryCreateModel model)
    {
        try
        {
            var cat = Mapper.Map<CategoryEntity>(model);
            
            // Save image
            string imageUrl = await _pictureService.Save(model.Image);
            cat.ImageUrl = imageUrl;
            
            await Context.AddAsync(cat);
            await Context.SaveChangesAsync();
            
            var mapped = Mapper.Map<CategoryModel>(cat);
            return mapped;
        }
        catch
        {
            return null;
        }
    }

    public async Task<CategoryModel?> Update(CategoryUpdateModel model)
    {
        try
        {
            var cat = await Context.Categories.FindAsync(model.Id) 
                      ?? throw new Exception("Category not found.");
            Mapper.Map(model, cat);
            
            // Unlinking files and saving a new picture
            if (model.Image is not null) {
                _pictureService.RemoveByUrl(cat.ImageUrl);
                string newImageUrl = await _pictureService.Save(model.Image);
                cat.ImageUrl = newImageUrl;
            }

            Context.Update(cat);
            await Context.SaveChangesAsync();

            var mapped = Mapper.Map<CategoryModel>(cat);
            return mapped;
        }
        catch
        {
            return null;
        }
    }

    public async Task<bool> Delete(int id)
    {
        var cat = await Context.Categories.FindAsync(id);
        if (cat is null) return false;
        
        // Unlinking files
        _pictureService.RemoveByUrl(cat.ImageUrl);
            
        Context.Remove(cat);
        await Context.SaveChangesAsync();
            
        return true;
    }
}
