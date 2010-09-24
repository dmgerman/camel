begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xstream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xstream
package|;
end_package

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
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
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
name|DefaultClassResolver
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|XStreamDataFormatOmitFieldsTest
specifier|public
class|class
name|XStreamDataFormatOmitFieldsTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testOmitPrice ()
specifier|public
name|void
name|testOmitPrice
parameter_list|()
block|{
name|PurchaseOrder
name|purchaseOrder
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|purchaseOrder
operator|.
name|setName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|purchaseOrder
operator|.
name|setPrice
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|XStreamDataFormat
name|xStreamDataFormat
init|=
operator|new
name|XStreamDataFormat
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|omitFields
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|omitFields
operator|.
name|put
argument_list|(
name|PurchaseOrder
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"price"
block|}
argument_list|)
expr_stmt|;
name|xStreamDataFormat
operator|.
name|setOmitFields
argument_list|(
name|omitFields
argument_list|)
expr_stmt|;
name|XStream
name|xStream
init|=
name|xStreamDataFormat
operator|.
name|createXStream
argument_list|(
operator|new
name|DefaultClassResolver
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|marshalledOrder
init|=
name|xStream
operator|.
name|toXML
argument_list|(
name|purchaseOrder
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|marshalledOrder
operator|.
name|contains
argument_list|(
literal|"<price>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

