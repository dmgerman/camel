begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|UnmarshalException
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CamelContext
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
name|TypeConverter
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
name|DefaultCamelContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JAXBConvertTest
specifier|public
class|class
name|JAXBConvertTest
extends|extends
name|TestCase
block|{
DECL|field|context
specifier|protected
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|converter
specifier|protected
name|TypeConverter
name|converter
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
DECL|method|testConverter ()
specifier|public
name|void
name|testConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|purchaseOrder
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|,
literal|"<purchaseOrder name='foo' amount='123.45' price='2.22'/>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Purchase order should not be null!"
argument_list|,
name|purchaseOrder
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
literal|"foo"
argument_list|,
name|purchaseOrder
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"amount"
argument_list|,
literal|123.45
argument_list|,
name|purchaseOrder
operator|.
name|getAmount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStreamShouldBeClosed ()
specifier|public
name|void
name|testStreamShouldBeClosed
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"<purchaseOrder name='foo' amount='123.45' price='2.22'/>"
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|PurchaseOrder
name|purchaseOrder
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|purchaseOrder
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|is
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStreamShouldBeClosedEvenForException ()
specifier|public
name|void
name|testStreamShouldBeClosedEvenForException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"<errorOrder name='foo' amount='123.45' price='2.22'/>"
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|converter
operator|.
name|convertTo
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|UnmarshalException
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|is
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

