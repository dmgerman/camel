begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_comment
comment|/**  * Represents a resolver for {@link FactoryFinder}  */
end_comment

begin_interface
DECL|interface|FactoryFinderResolver
specifier|public
interface|interface
name|FactoryFinderResolver
block|{
comment|/**      * Creates a new default factory finder using a default resource path.      *      * @param classResolver the class resolver to use      * @return a factory finder.      */
DECL|method|resolveDefaultFactoryFinder (ClassResolver classResolver)
name|FactoryFinder
name|resolveDefaultFactoryFinder
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|)
function_decl|;
comment|/**      * Creates a new factory finder.      *      * @param classResolver the class resolver to use      * @param resourcePath the resource path as base to lookup files within      * @return a factory finder.      */
DECL|method|resolveFactoryFinder (ClassResolver classResolver, String resourcePath)
name|FactoryFinder
name|resolveFactoryFinder
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|,
name|String
name|resourcePath
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

