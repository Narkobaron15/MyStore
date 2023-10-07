namespace MyStoreBack.Data.Entity.Base;

public interface IEntity<T>
{
    T Id { get; set; }
    bool IsDeleted { get; set; }
    DateTime CreatedAt { get; set; }
}