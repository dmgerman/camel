begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|iec60870
operator|.
name|BaseOptions
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|ProtocolOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|client
operator|.
name|data
operator|.
name|DataModuleOptions
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|ClientOptions
specifier|public
class|class
name|ClientOptions
extends|extends
name|BaseOptions
argument_list|<
name|ClientOptions
argument_list|>
block|{
comment|/**      * Data module options      */
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"org.eclipse.neoscada.protocol.iec60870.client.data.DataModuleOptions"
argument_list|)
DECL|field|dataModuleOptions
specifier|private
name|DataModuleOptions
operator|.
name|Builder
name|dataModuleOptions
decl_stmt|;
comment|// dummy for doc generation
comment|/**      * Whether background scan transmissions should be ignored.      */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"data"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|ignoreBackgroundScan
specifier|private
name|boolean
name|ignoreBackgroundScan
init|=
literal|true
decl_stmt|;
comment|// dummy for doc generation
comment|/**      * Whether to include the source address      */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"data"
argument_list|)
DECL|field|causeSourceAddress
specifier|private
name|byte
name|causeSourceAddress
decl_stmt|;
DECL|method|ClientOptions ()
specifier|public
name|ClientOptions
parameter_list|()
block|{
name|this
operator|.
name|dataModuleOptions
operator|=
operator|new
name|DataModuleOptions
operator|.
name|Builder
argument_list|()
expr_stmt|;
block|}
DECL|method|ClientOptions (final ClientOptions other)
specifier|public
name|ClientOptions
parameter_list|(
specifier|final
name|ClientOptions
name|other
parameter_list|)
block|{
name|this
argument_list|(
name|other
operator|.
name|getProtocolOptions
argument_list|()
argument_list|,
name|other
operator|.
name|getDataModuleOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ClientOptions (final ProtocolOptions protocolOptions, final DataModuleOptions dataOptions)
specifier|public
name|ClientOptions
parameter_list|(
specifier|final
name|ProtocolOptions
name|protocolOptions
parameter_list|,
specifier|final
name|DataModuleOptions
name|dataOptions
parameter_list|)
block|{
name|super
argument_list|(
name|protocolOptions
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|dataOptions
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataModuleOptions
operator|=
operator|new
name|DataModuleOptions
operator|.
name|Builder
argument_list|(
name|dataOptions
argument_list|)
expr_stmt|;
block|}
DECL|method|setDataModuleOptions (final DataModuleOptions dataModuleOptions)
specifier|public
name|void
name|setDataModuleOptions
parameter_list|(
specifier|final
name|DataModuleOptions
name|dataModuleOptions
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|dataModuleOptions
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataModuleOptions
operator|=
operator|new
name|DataModuleOptions
operator|.
name|Builder
argument_list|(
name|dataModuleOptions
argument_list|)
expr_stmt|;
block|}
DECL|method|getDataModuleOptions ()
specifier|public
name|DataModuleOptions
name|getDataModuleOptions
parameter_list|()
block|{
return|return
name|this
operator|.
name|dataModuleOptions
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|ClientOptions
name|copy
parameter_list|()
block|{
return|return
operator|new
name|ClientOptions
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|// wrapper methods - DataModuleOptions
DECL|method|getCauseSourceAddress ()
specifier|public
name|byte
name|getCauseSourceAddress
parameter_list|()
block|{
return|return
name|causeSourceAddress
return|;
block|}
DECL|method|setCauseSourceAddress (final byte causeSourceAddress)
specifier|public
name|void
name|setCauseSourceAddress
parameter_list|(
specifier|final
name|byte
name|causeSourceAddress
parameter_list|)
block|{
name|this
operator|.
name|causeSourceAddress
operator|=
name|causeSourceAddress
expr_stmt|;
name|this
operator|.
name|dataModuleOptions
operator|.
name|setCauseSourceAddress
argument_list|(
name|causeSourceAddress
argument_list|)
expr_stmt|;
block|}
DECL|method|setIgnoreBackgroundScan (final boolean ignoreBackgroundScan)
specifier|public
name|void
name|setIgnoreBackgroundScan
parameter_list|(
specifier|final
name|boolean
name|ignoreBackgroundScan
parameter_list|)
block|{
name|this
operator|.
name|dataModuleOptions
operator|.
name|setIgnoreBackgroundScan
argument_list|(
name|ignoreBackgroundScan
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreBackgroundScan ()
specifier|public
name|boolean
name|isIgnoreBackgroundScan
parameter_list|()
block|{
return|return
name|this
operator|.
name|dataModuleOptions
operator|.
name|isIgnoreBackgroundScan
argument_list|()
return|;
block|}
block|}
end_class

end_unit

