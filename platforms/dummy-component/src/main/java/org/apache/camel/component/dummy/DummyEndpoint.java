begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dummy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dummy
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
name|support
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
name|spi
operator|.
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"dummy"
argument_list|,
name|syntax
operator|=
literal|"dummy:drink"
argument_list|,
name|title
operator|=
literal|"Dummy"
argument_list|,
name|label
operator|=
literal|"bar"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|)
DECL|class|DummyEndpoint
specifier|public
class|class
name|DummyEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|drink
specifier|private
name|Drinks
name|drink
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|amount
specifier|private
name|int
name|amount
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
DECL|field|celebrity
specifier|private
name|boolean
name|celebrity
decl_stmt|;
DECL|method|DummyEndpoint (String endpointUri, Component component)
specifier|public
name|DummyEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
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
name|DummyProducer
argument_list|(
name|this
argument_list|,
name|drink
argument_list|,
name|amount
argument_list|,
name|celebrity
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
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
DECL|method|getDrink ()
specifier|public
name|Drinks
name|getDrink
parameter_list|()
block|{
return|return
name|drink
return|;
block|}
comment|/**      * What drink to order      */
DECL|method|setDrink (Drinks drink)
specifier|public
name|void
name|setDrink
parameter_list|(
name|Drinks
name|drink
parameter_list|)
block|{
name|this
operator|.
name|drink
operator|=
name|drink
expr_stmt|;
block|}
DECL|method|getAmount ()
specifier|public
name|int
name|getAmount
parameter_list|()
block|{
return|return
name|amount
return|;
block|}
comment|/**      * Number of drinks in the order      */
DECL|method|setAmount (int amount)
specifier|public
name|void
name|setAmount
parameter_list|(
name|int
name|amount
parameter_list|)
block|{
name|this
operator|.
name|amount
operator|=
name|amount
expr_stmt|;
block|}
DECL|method|isCelebrity ()
specifier|public
name|boolean
name|isCelebrity
parameter_list|()
block|{
return|return
name|celebrity
return|;
block|}
comment|/**      * Is this a famous person ordering      */
DECL|method|setCelebrity (boolean celebrity)
specifier|public
name|void
name|setCelebrity
parameter_list|(
name|boolean
name|celebrity
parameter_list|)
block|{
name|this
operator|.
name|celebrity
operator|=
name|celebrity
expr_stmt|;
block|}
block|}
end_class

end_unit

