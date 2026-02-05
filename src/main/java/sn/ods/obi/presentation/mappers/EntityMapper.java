package sn.ods.obi.presentation.mappers;


import org.mapstruct.InheritConfiguration;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;


@MapperConfig
public interface EntityMapper<E, DRQ, DRP> {

    DRP toDto(E entity);

        List<DRP> toDtoList(List<E> entityList);

    Set<DRP> toDtoSet(Set<E> entityList);

    default Page<DRP> toDtoPage(Page<E> entityPage) {
        Pageable pageable = entityPage.getPageable();
        List<DRP> dtoList = toDtoList(entityPage.getContent());
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    E toEntity(DRQ dto);

    List<E> toEntityList(List<DRQ> dtoList);

    Set<E> toEntitySet(Set<DRQ> dtoList);

    @InheritConfiguration
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DRQ dto, @MappingTarget E entity);

    @InheritConfiguration
    void updateDtoFromEntity(E entity, @MappingTarget DRQ dto);
}
