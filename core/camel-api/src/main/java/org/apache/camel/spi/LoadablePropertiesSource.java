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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
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
name|Ordered
import|;
end_import

begin_comment
comment|/**  * A source for properties that can be loaded all at once during initialization,  * such as loading .properties files.  *<p/>  * A source can implement {@link Ordered} to control the ordering of which sources are used by the Camel  * properties component. The source with the highest precedence (lowest number) will be used first.  */
end_comment

begin_interface
DECL|interface|LoadablePropertiesSource
specifier|public
interface|interface
name|LoadablePropertiesSource
extends|extends
name|PropertiesSource
block|{
comment|/**      * Loads the properties from the source      *      * @return the loaded properties      */
DECL|method|loadProperties ()
name|Properties
name|loadProperties
parameter_list|()
function_decl|;
comment|/**      * Loads the properties from the source filtering them out according to a predicate.      *      * @param filter the predicate used to filter out properties based on the key.      * @return the properties loaded.      */
DECL|method|loadProperties (Predicate<String> filter)
name|Properties
name|loadProperties
parameter_list|(
name|Predicate
argument_list|<
name|String
argument_list|>
name|filter
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

