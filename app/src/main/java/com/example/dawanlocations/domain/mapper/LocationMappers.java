package com.example.dawanlocations.domain.mapper;

import com.example.dawanlocations.data.local.LocationEntity;
import com.example.dawanlocations.data.remote.model.LocationDto;
import com.example.dawanlocations.domain.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire regroupant les méthodes de conversion (mapping)
 * entre les différents modèles de données utilisés dans l’application :
 * <ul>
 *     <li>{@link LocationDto} : modèle venant de l’API distante (données brutes JSON)</li>
 *     <li>{@link LocationEntity} : entité Room stockée en base locale</li>
 *     <li>{@link Location} : modèle de domaine utilisé dans l’application (UI, logique)</li>
 * </ul>
 *
 * <p>
 * Cette classe est déclarée <b>final</b> et possède un constructeur privé
 * car elle ne contient que des méthodes statiques.
 * </p>
 */
public final class LocationMappers {

    /** Constructeur privé pour empêcher l’instanciation. */
    private LocationMappers() {}

    /**
     * Convertit un {@link LocationDto} (provenant de l’API REST)
     * en {@link LocationEntity} (pour stockage en base locale).
     *
     * @param dto l’objet reçu depuis l’API
     * @return une nouvelle {@link LocationEntity} contenant les mêmes données
     */
    public static LocationEntity toEntity(LocationDto dto) {
        LocationEntity e = new LocationEntity();
        e.id = dto.id;
        e.name = dto.name;
        e.address = dto.address;
        e.city = dto.city;
        e.postalCode = dto.postalCode;
        e.latitude = dto.latitude;
        e.longitude = dto.longitude;
        return e;
    }

    /**
     * Convertit une {@link LocationEntity} (depuis Room)
     * en {@link Location} (modèle de domaine).
     *
     * @param e l’entité Room
     * @return un objet {@link Location} prêt à être utilisé par la logique applicative ou l’UI
     */
    public static Location toDomain(LocationEntity e) {
        return new Location(e.id, e.name, e.address, e.city, e.postalCode, e.latitude, e.longitude);
    }

    /**
     * Convertit une liste d’entités {@link LocationEntity}
     * en liste de modèles de domaine {@link Location}.
     *
     * @param list la liste d’entités Room (peut être {@code null})
     * @return une nouvelle liste de {@link Location} (jamais {@code null})
     */
    public static List<Location> toDomainList(List<LocationEntity> list) {
        List<Location> out = new ArrayList<>();
        if (list != null) {
            for (LocationEntity e : list) {
                out.add(toDomain(e));
            }
        }
        return out;
    }
}
