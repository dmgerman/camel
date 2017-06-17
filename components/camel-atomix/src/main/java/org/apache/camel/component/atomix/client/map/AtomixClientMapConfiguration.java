begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.map
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|map
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientAction
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
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConfiguration
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
name|UriParam
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
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|AtomixClientMapConfiguration
specifier|public
class|class
name|AtomixClientMapConfiguration
extends|extends
name|AtomixClientConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"PUT"
argument_list|)
DECL|field|defaultAction
specifier|private
name|AtomixClientAction
name|defaultAction
init|=
name|AtomixClientAction
operator|.
name|PUT
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ttl
specifier|private
name|Long
name|ttl
decl_stmt|;
annotation|@
name|UriParam
DECL|field|resultHeader
specifier|private
name|String
name|resultHeader
decl_stmt|;
comment|//    @UriParam(label = "advanced")
comment|//    private DistributedMap.Config config = new DistributedMap.Config();
comment|//    @UriParam(label = "advanced")
comment|//    private DistributedMap.Options options = new DistributedMap.Options();
comment|// ****************************************
comment|// Properties
comment|// ****************************************
DECL|method|getDefaultAction ()
specifier|public
name|AtomixClientAction
name|getDefaultAction
parameter_list|()
block|{
return|return
name|defaultAction
return|;
block|}
comment|/**      * The default action.      */
DECL|method|setDefaultAction (AtomixClientAction defaultAction)
specifier|public
name|void
name|setDefaultAction
parameter_list|(
name|AtomixClientAction
name|defaultAction
parameter_list|)
block|{
name|this
operator|.
name|defaultAction
operator|=
name|defaultAction
expr_stmt|;
block|}
DECL|method|getTtl ()
specifier|public
name|Long
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
comment|/**      * The resource ttl.      */
DECL|method|setTtl (Long ttl)
specifier|public
name|void
name|setTtl
parameter_list|(
name|Long
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
expr_stmt|;
block|}
DECL|method|getResultHeader ()
specifier|public
name|String
name|getResultHeader
parameter_list|()
block|{
return|return
name|resultHeader
return|;
block|}
comment|/**      * The header that wil carry the result.      */
DECL|method|setResultHeader (String resultHeader)
specifier|public
name|void
name|setResultHeader
parameter_list|(
name|String
name|resultHeader
parameter_list|)
block|{
name|this
operator|.
name|resultHeader
operator|=
name|resultHeader
expr_stmt|;
block|}
comment|//    public DistributedMap.Config getConfig() {
comment|//        return config;
comment|//    }
comment|//
comment|//    /**
comment|//     * The cluster wide map config
comment|//     */
comment|//    public void setConfig(DistributedMap.Config config) {
comment|//        this.config = config;
comment|//    }
comment|//
comment|//    public DistributedMap.Options getOptions() {
comment|//        return options;
comment|//    }
comment|//
comment|//    /**
comment|//     * The local map options
comment|//     */
comment|//    public void setOptions(DistributedMap.Options options) {
comment|//        this.options = options;
comment|//    }
comment|// ****************************************
comment|// Copy
comment|// ****************************************
DECL|method|copy ()
specifier|public
name|AtomixClientMapConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|AtomixClientMapConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

