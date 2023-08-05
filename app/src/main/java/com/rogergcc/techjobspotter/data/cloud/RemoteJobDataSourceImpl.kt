package com.rogergcc.techjobspotter.data.cloud

import com.rogergcc.techjobspotter.data.cloud.model.JobDto
import com.rogergcc.techjobspotter.domain.JobRemoteDataSource


/**
 * Created on agosto.
 * year 2023 .
 */
class RemoteJobDataSourceImpl : JobRemoteDataSource {
    // Implementa la lógica para obtener los datos remotos
    override suspend fun geJobs(): List<JobDto> {
        // Código para obtener los datos remotos
        // ...

        return listOf(JobDto(), JobDto())
//        return listOf(/* lista de JobDto obtenidos desde la fuente remota */)
//        return emptyList()
    }
}