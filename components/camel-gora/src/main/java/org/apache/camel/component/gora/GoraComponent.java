begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gora
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gora
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|Properties
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
name|Endpoint
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
name|annotations
operator|.
name|Component
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|persistency
operator|.
name|Persistent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|store
operator|.
name|DataStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|store
operator|.
name|DataStoreFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gora
operator|.
name|GoraConstants
operator|.
name|GORA_DEFAULT_DATASTORE_KEY
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"gora"
argument_list|)
DECL|class|GoraComponent
specifier|public
class|class
name|GoraComponent
extends|extends
name|DefaultComponent
block|{
comment|/**      * GORA datastore      */
DECL|field|dataStore
specifier|private
name|DataStore
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|dataStore
decl_stmt|;
comment|/**      * GORA properties      */
DECL|field|goraProperties
specifier|private
name|Properties
name|goraProperties
decl_stmt|;
comment|/**      * Hadoop configuration      */
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
DECL|method|GoraComponent ()
specifier|public
name|GoraComponent
parameter_list|()
block|{     }
comment|/**      *      * Initialize class and create DataStore instance      *      * @param config component configuration      * @throws IOException      */
DECL|method|init (final GoraConfiguration config)
specifier|private
name|void
name|init
parameter_list|(
specifier|final
name|GoraConfiguration
name|config
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|goraProperties
operator|=
name|DataStoreFactory
operator|.
name|createProps
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataStore
operator|=
name|DataStoreFactory
operator|.
name|getDataStore
argument_list|(
name|goraProperties
operator|.
name|getProperty
argument_list|(
name|GORA_DEFAULT_DATASTORE_KEY
argument_list|,
name|config
operator|.
name|getDataStoreClass
argument_list|()
argument_list|)
argument_list|,
name|config
operator|.
name|getKeyClass
argument_list|()
argument_list|,
name|config
operator|.
name|getValueClass
argument_list|()
argument_list|,
name|this
operator|.
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|GoraConfiguration
name|config
init|=
operator|new
name|GoraConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|config
operator|.
name|setName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
operator|new
name|GoraEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|,
name|dataStore
argument_list|)
return|;
block|}
comment|/**      * Get DataStore      */
DECL|method|getDataStore ()
specifier|public
name|DataStore
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|getDataStore
parameter_list|()
block|{
return|return
name|dataStore
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|Configuration
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|dataStore
operator|!=
literal|null
condition|)
block|{
name|dataStore
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

