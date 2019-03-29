/*
 *  Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.database;

import club.spreadme.database.annotation.*;
import club.spreadme.database.plugin.paginator.Page;
import club.spreadme.domain.Movie;

import java.util.List;

public interface MovieDao {

    @Query("select * from movies where id = #{id}")
    Movie getMovieById(String id);

    @Transactional
    @Query(value = "select * from movies where id = '${id}' and type = #{type} and rating = ${rating}",
            processor = MovieDaoPostProcessor.class)
    List<Movie> getMovieById(@Param("id") String id, String type, @Param("rating") Double rating);

    @Query("select * from movies where primarytitle = #{primarytitle} and type = '${type}'")
    List<Movie> getMoviesByName(String primarytitle, @Param("type") String type, Page<Movie> page);

    @Insert
    @Transactional
    int insert(Movie movie);

    @Update
    @Transactional
    int update(Movie movie);

    @Delete("delete from movies where id = #{id}")
    int delete(String id);
}
