begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Configuration use by {@link org.apache.camel.spi.RestConsumerFactory} for Camel components to support  * the Camel {@link org.apache.camel.model.rest.RestDefinition rest} DSL.  */
end_comment

begin_class
DECL|class|RestConfiguration
specifier|public
class|class
name|RestConfiguration
block|{
DECL|field|component
specifier|private
name|String
name|component
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|properties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
comment|/**      * Gets the name of the Camel component to use as the REST consumer      *      * @return the component name, or<tt>null</tt> to let Camel search the {@link Registry} to find suitable implementation      */
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
comment|/**      * Sets the name of the Camel component to use as the REST consumer      *      * @param componentName the name of the component (such as restlet, spark-rest, etc.)      */
DECL|method|setComponent (String componentName)
specifier|public
name|void
name|setComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|componentName
expr_stmt|;
block|}
comment|/**      * Gets the hostname to use by the REST consumer      *      * @return the hostname, or<tt>null</tt> to use default hostname      */
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * Sets the hostname to use by the REST consumer      *      * @param host the hostname      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
comment|/**      * Gets the port to use by the REST consumer      *      * @return the port, or<tt>0</tt> or<tt>-1</tt> to use default port      */
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Gets the port to use by the REST consumer      *      * @param port the port number      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
comment|/**      * Gets additional options to use to configure the REST consumer      *      * @return additional options      */
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * Sets additional options to use to configure the REST consumer      *      * @param properties the options      */
DECL|method|setProperties (Map<String, Object> properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
block|}
end_class

end_unit

