begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sns
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sns
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sns
operator|.
name|model
operator|.
name|MessageAttributeValue
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
name|junit
operator|.
name|Before
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|SnsProducerTest
specifier|public
class|class
name|SnsProducerTest
block|{
annotation|@
name|Mock
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Mock
DECL|field|endpoint
specifier|private
name|SnsEndpoint
name|endpoint
decl_stmt|;
DECL|field|producer
specifier|private
name|SnsProducer
name|producer
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|producer
operator|=
operator|new
name|SnsProducer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|SnsHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|translateAttributes ()
specifier|public
name|void
name|translateAttributes
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key3"
argument_list|,
literal|"value3"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key4"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Value4"
argument_list|,
literal|"Value5"
argument_list|,
literal|"Value6"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key5"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Value7"
argument_list|,
literal|null
argument_list|,
literal|"Value9"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key6"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|10
argument_list|,
literal|null
argument_list|,
literal|12
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key7"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|MessageAttributeValue
argument_list|>
name|translateAttributes
init|=
name|producer
operator|.
name|translateAttributes
argument_list|(
name|headers
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|translateAttributes
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|translateAttributes
operator|.
name|get
argument_list|(
literal|"key3"
argument_list|)
operator|.
name|getDataType
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"String"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|translateAttributes
operator|.
name|get
argument_list|(
literal|"key3"
argument_list|)
operator|.
name|getStringValue
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"value3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|translateAttributes
operator|.
name|get
argument_list|(
literal|"key4"
argument_list|)
operator|.
name|getDataType
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"String.Array"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|translateAttributes
operator|.
name|get
argument_list|(
literal|"key4"
argument_list|)
operator|.
name|getStringValue
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"[\"Value4\", \"Value5\", \"Value6\"]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|translateAttributes
operator|.
name|get
argument_list|(
literal|"key5"
argument_list|)
operator|.
name|getStringValue
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"[\"Value7\", null, \"Value9\"]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|translateAttributes
operator|.
name|get
argument_list|(
literal|"key6"
argument_list|)
operator|.
name|getStringValue
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"[10, null, 12]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|translateAttributes
operator|.
name|get
argument_list|(
literal|"key7"
argument_list|)
operator|.
name|getStringValue
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"[true, null, false]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

