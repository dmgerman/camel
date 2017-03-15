begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|HeaderFilterStrategyComponent
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
name|spi
operator|.
name|Metadata
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
name|util
operator|.
name|CamelContextHelper
import|;
end_import

begin_class
DECL|class|HttpCommonComponent
specifier|public
specifier|abstract
class|class
name|HttpCommonComponent
extends|extends
name|HeaderFilterStrategyComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom HttpBinding to control the mapping between Camel message and HttpClient."
argument_list|)
DECL|field|httpBinding
specifier|protected
name|HttpBinding
name|httpBinding
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use the shared HttpConfiguration as base configuration."
argument_list|)
DECL|field|httpConfiguration
specifier|protected
name|HttpConfiguration
name|httpConfiguration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Whether to allow java serialization when a request uses context-type=application/x-java-serialized-object."
operator|+
literal|" This is by default turned off. "
operator|+
literal|" If you enable this then be aware that Java will deserialize the incoming data from the request to Java and that can be a potential security risk."
argument_list|)
DECL|field|allowJavaSerializedObject
specifier|protected
name|boolean
name|allowJavaSerializedObject
decl_stmt|;
DECL|method|HttpCommonComponent (Class<? extends HttpCommonEndpoint> endpointClass)
specifier|public
name|HttpCommonComponent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|HttpCommonEndpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|super
argument_list|(
name|endpointClass
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the parameter. This method doesn't resolve reference parameters in the registry.      *      * @param parameters    the parameters      * @param key           the key      * @param type          the requested type to convert the value from the parameter      * @return  the converted value parameter      */
DECL|method|getParameter (Map<String, Object> parameters, String key, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getParameter
argument_list|(
name|parameters
argument_list|,
name|key
argument_list|,
name|type
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Gets the parameter. This method doesn't resolve reference parameters in the registry.      *      * @param parameters    the parameters      * @param key           the key      * @param type          the requested type to convert the value from the parameter      * @param defaultValue  use this default value if the parameter does not contain the key      * @return  the converted value parameter      */
DECL|method|getParameter (Map<String, Object> parameters, String key, Class<T> type, T defaultValue)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|defaultValue
parameter_list|)
block|{
name|Object
name|value
init|=
name|parameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|defaultValue
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Connects the URL specified on the endpoint to the specified processor.      *      * @param consumer the consumer      * @throws Exception can be thrown      */
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{     }
comment|/**      * Disconnects the URL specified on the endpoint from the specified processor.      *      * @param consumer the consumer      * @throws Exception can be thrown      */
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
DECL|method|useIntrospectionOnEndpoint ()
specifier|protected
name|boolean
name|useIntrospectionOnEndpoint
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getHttpBinding ()
specifier|public
name|HttpBinding
name|getHttpBinding
parameter_list|()
block|{
return|return
name|httpBinding
return|;
block|}
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message and HttpClient.      */
DECL|method|setHttpBinding (HttpBinding httpBinding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|HttpBinding
name|httpBinding
parameter_list|)
block|{
name|this
operator|.
name|httpBinding
operator|=
name|httpBinding
expr_stmt|;
block|}
DECL|method|getHttpConfiguration ()
specifier|public
name|HttpConfiguration
name|getHttpConfiguration
parameter_list|()
block|{
return|return
name|httpConfiguration
return|;
block|}
comment|/**      * To use the shared HttpConfiguration as base configuration.      */
DECL|method|setHttpConfiguration (HttpConfiguration httpConfiguration)
specifier|public
name|void
name|setHttpConfiguration
parameter_list|(
name|HttpConfiguration
name|httpConfiguration
parameter_list|)
block|{
name|this
operator|.
name|httpConfiguration
operator|=
name|httpConfiguration
expr_stmt|;
block|}
DECL|method|isAllowJavaSerializedObject ()
specifier|public
name|boolean
name|isAllowJavaSerializedObject
parameter_list|()
block|{
return|return
name|allowJavaSerializedObject
return|;
block|}
comment|/**      * Whether to allow java serialization when a request uses context-type=application/x-java-serialized-object      *<p/>      * This is by default turned off. If you enable this then be aware that Java will deserialize the incoming      * data from the request to Java and that can be a potential security risk.      */
DECL|method|setAllowJavaSerializedObject (boolean allowJavaSerializedObject)
specifier|public
name|void
name|setAllowJavaSerializedObject
parameter_list|(
name|boolean
name|allowJavaSerializedObject
parameter_list|)
block|{
name|this
operator|.
name|allowJavaSerializedObject
operator|=
name|allowJavaSerializedObject
expr_stmt|;
block|}
block|}
end_class

end_unit

