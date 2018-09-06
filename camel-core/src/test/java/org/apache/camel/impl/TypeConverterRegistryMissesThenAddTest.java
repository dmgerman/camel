begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|support
operator|.
name|TypeConverterSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|TypeConverterRegistryMissesThenAddTest
specifier|public
class|class
name|TypeConverterRegistryMissesThenAddTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testMissThenAddTypeConverter ()
specifier|public
name|void
name|testMissThenAddTypeConverter
parameter_list|()
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|MyOrder
name|order
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|MyOrder
operator|.
name|class
argument_list|,
literal|"123"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|order
argument_list|)
expr_stmt|;
comment|// add missing type converter
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|MyOrder
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|MyOrderTypeConverter
argument_list|()
argument_list|)
expr_stmt|;
comment|// this time it should work
name|order
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|MyOrder
operator|.
name|class
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|order
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|order
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyOrder
specifier|private
specifier|static
class|class
name|MyOrder
block|{
DECL|field|id
specifier|private
name|int
name|id
decl_stmt|;
DECL|method|getId ()
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (int id)
specifier|public
name|void
name|setId
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
block|}
DECL|class|MyOrderTypeConverter
specifier|private
specifier|static
class|class
name|MyOrderTypeConverter
extends|extends
name|TypeConverterSupport
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// converter from value to the MyOrder bean
name|MyOrder
name|order
init|=
operator|new
name|MyOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setId
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|(
name|T
operator|)
name|order
return|;
block|}
block|}
block|}
end_class

end_unit

