begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RoutesBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|RoutesDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
operator|.
name|RestsDefinition
import|;
end_import

begin_comment
comment|/**  * Collects routes and rests from the various sources (like registry or opinionated  * classpath locations) and adds these into the Camel context.  */
end_comment

begin_interface
DECL|interface|RoutesCollector
specifier|public
interface|interface
name|RoutesCollector
block|{
comment|/**      * Collects the {@link RoutesBuilder} instances which was discovered from the {@link org.apache.camel.spi.Registry} such as      * Spring or CDI bean containers.      *      * @param camelContext        the Camel Context      * @param excludePattern      exclude pattern (see javaRoutesExcludePattern option)      * @param includePattern      include pattern  (see javaRoutesIncludePattern option)      * @return the discovered routes or an empty list      */
DECL|method|collectRoutesFromRegistry (CamelContext camelContext, String excludePattern, String includePattern)
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|collectRoutesFromRegistry
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|excludePattern
parameter_list|,
name|String
name|includePattern
parameter_list|)
function_decl|;
comment|/**      * Collects all XML routes from the given directory.      *      * @param camelContext               the Camel Context      * @param directory                  the directory (see xmlRoutes option)      * @return the discovered routes or an empty list      */
DECL|method|collectXmlRoutesFromDirectory (CamelContext camelContext, String directory)
name|List
argument_list|<
name|RoutesDefinition
argument_list|>
name|collectXmlRoutesFromDirectory
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|directory
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Collects all XML rests from the given directory.      *      * @param camelContext               the Camel Context      * @param directory                  the directory (see xmlRests option)      * @return the discovered rests or an empty list      */
DECL|method|collectXmlRestsFromDirectory (CamelContext camelContext, String directory)
name|List
argument_list|<
name|RestsDefinition
argument_list|>
name|collectXmlRestsFromDirectory
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|directory
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

