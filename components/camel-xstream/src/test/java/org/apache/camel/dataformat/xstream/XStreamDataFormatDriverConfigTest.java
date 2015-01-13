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
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|json
operator|.
name|JsonHierarchicalStreamDriver
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

begin_class
DECL|class|XStreamDataFormatDriverConfigTest
specifier|public
class|class
name|XStreamDataFormatDriverConfigTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testJson ()
specifier|public
name|void
name|testJson
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
name|XStreamDataFormat
name|xStreamDataFormat
init|=
operator|new
name|XStreamDataFormat
argument_list|()
decl_stmt|;
name|xStreamDataFormat
operator|.
name|setXstreamDriver
argument_list|(
operator|new
name|JsonHierarchicalStreamDriver
argument_list|()
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
name|assertEquals
argument_list|(
literal|"{"
argument_list|,
name|marshalledOrder
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

