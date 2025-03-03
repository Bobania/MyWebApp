package bakery.mapper;

public interface Mapper <T, D>{
D toDto(T dto);
T toEntity(D entity);
}
