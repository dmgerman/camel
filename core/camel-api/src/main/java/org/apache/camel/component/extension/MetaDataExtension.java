begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|Exchange
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
name|TypeConversionException
import|;
end_import

begin_interface
DECL|interface|MetaDataExtension
specifier|public
interface|interface
name|MetaDataExtension
extends|extends
name|ComponentExtension
block|{
comment|/**      * @param parameters      * @return the {@link MetaData}      */
DECL|method|meta (Map<String, Object> parameters)
name|Optional
argument_list|<
name|MetaData
argument_list|>
name|meta
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
function_decl|;
DECL|interface|MetaData
interface|interface
name|MetaData
block|{
comment|// Common meta-data attributes
DECL|field|CONTENT_TYPE
name|String
name|CONTENT_TYPE
init|=
name|Exchange
operator|.
name|CONTENT_TYPE
decl_stmt|;
DECL|field|JAVA_TYPE
name|String
name|JAVA_TYPE
init|=
literal|"Java-Type"
decl_stmt|;
DECL|field|CONTEXT
name|String
name|CONTEXT
init|=
literal|"Meta-Context"
decl_stmt|;
comment|/**          * Returns an attribute associated with this meta data by name.          *          * @param name the attribute name          * @return the attribute          */
DECL|method|getAttribute (String name)
name|Object
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**          * @return a read-only list of attributes.          */
DECL|method|getAttributes ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
comment|/**          *          * Returns an attribute associated with this meta data by name and          * specifying the type required.          *          * @param name the attribute name          * @param type the type of the attribute          * @return the value of the given attribute or<tt>null</tt> if there is no attribute for the given name          * @throws TypeConversionException is thrown if error during type conversion          */
DECL|method|getAttribute (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**          * Returns the payload of the meta data as a POJO.          *          * @return the body, can be<tt>null</tt>          */
DECL|method|getPayload ()
name|Object
name|getPayload
parameter_list|()
function_decl|;
comment|/**          * Returns the payload of the meta data as specified type.          *          * @param type the type that the payload should be converted yo.          * @return the payload of the meta data as the specified type.          * @throws TypeConversionException is thrown if error during type conversion          */
DECL|method|getPayload (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getPayload
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

