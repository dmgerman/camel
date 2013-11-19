begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Processor
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
name|Producer
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
name|DefaultEndpoint
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Gora endpoint  *  * @author ipolyzos  */
end_comment

begin_class
DECL|class|GoraEndpoint
specifier|public
class|class
name|GoraEndpoint
extends|extends
name|DefaultEndpoint
block|{
comment|/**      * logger      */
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GoraEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Gora DataStore      */
DECL|field|dataStore
specifier|private
specifier|final
name|DataStore
name|dataStore
decl_stmt|;
comment|/**      * Camel-Gora Endpoint Configuratopn      */
DECL|field|configuration
specifier|private
name|GoraConfiguration
name|configuration
decl_stmt|;
comment|/**      * GORA endpoint default constructor      *      * @param uri           Endpoint URI      * @param goraComponent Reference to the Camel-Gora component      * @param config        Reference to Camel-Gora endpoint configuration      * @param dataStore     Reference to Gora DataStore      */
DECL|method|GoraEndpoint (final String uri, final GoraComponent goraComponent, final GoraConfiguration config, final DataStore dataStore)
specifier|public
name|GoraEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|GoraComponent
name|goraComponent
parameter_list|,
specifier|final
name|GoraConfiguration
name|config
parameter_list|,
specifier|final
name|DataStore
name|dataStore
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|goraComponent
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|dataStore
operator|=
name|dataStore
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|GoraProducer
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|configuration
argument_list|,
name|this
operator|.
name|dataStore
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createConsumer (final Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
comment|//throw new UnsupportedOperationException("Not supported");
return|return
operator|new
name|GoraConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|this
operator|.
name|configuration
argument_list|,
name|this
operator|.
name|dataStore
argument_list|)
return|;
block|}
comment|/**      * Get Configutation      *      * @return      */
DECL|method|getConfiguration ()
specifier|public
name|GoraConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

