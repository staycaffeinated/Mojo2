/*
 * Copyright 2020 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.mojo.restapi.shared;

/**
 * The mode indicates the communication style of the controllers or handlers.
 * The 'standard' mode is for blocking APIs; this is probably the most common style
 * for CRUD applications. In this mode, Spring-MVC is used.
 * The 'reactive' and 'functional' are for asynchronous, non-blocking APIs.
 * A common use for this is dashboards that continually receive data updates,
 * such as a stock ticker. In these two modes, Spring-Webflux is used.
 *
 * In 'reactive' mode, Controllers are generated that use reactive streams.
 * In 'functional' mode, Handlers and Routers are generated, giving a functional style
 * to the backend code that handles requests.
 * 
 */
public enum ProgrammingModel {
    BLOCKING("blocking"),
    REACTIVE ("reactive"),
    FUNCTIONAL ("functional")
    ;

    // This is the value an end-user enters on the command line.
    private final String modelName;

    ProgrammingModel(String name) { this.modelName = name; }

    @Override
    public String toString() { return modelName; }
}
