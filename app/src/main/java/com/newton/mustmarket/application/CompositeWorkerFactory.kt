package com.newton.mustmarket.application

import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkerFactory

class CompositeWorkerFactory(
    factories: List<WorkerFactory>
) : DelegatingWorkerFactory() {
    init {
        factories.forEach { addFactory(it) }
    }
}