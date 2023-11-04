using AutoMapper;
using MyStoreBack.Data.Entity;
using MyStoreBack.Data.Entity.Identity;
using MyStoreBack.Models.Category;
using MyStoreBack.Models.Identity;

namespace MyStoreBack.Mapper;

public class MapperProfile : Profile
{
    public MapperProfile()
    {
        CreateMap<CategoryEntity, CategoryModel>()
            .ForMember(x => x.Image, opt => opt.MapFrom(src => src.ImageUrl));

        CreateMap<CategoryModel, CategoryEntity>()
            .ForMember(x => x.ImageUrl, opt => opt.MapFrom(src => src.Image));

        CreateMap<CategoryCreateModel, CategoryEntity>();
        CreateMap<CategoryUpdateModel, CategoryEntity>();

        CreateMap<RegisterModel, UserEntity>().ForMember(
            x => x.Image,
            opt => opt.Ignore()
        );
    }
}