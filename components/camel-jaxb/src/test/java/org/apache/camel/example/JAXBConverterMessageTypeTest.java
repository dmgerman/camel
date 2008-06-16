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
name|converter
operator|.
name|jaxb
operator|.
name|MessageType
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
comment|/**  * Unit test for JABX conversion of MessageType  */
end_comment

begin_class
DECL|class|JAXBConverterMessageTypeTest
specifier|public
class|class
name|JAXBConverterMessageTypeTest
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
comment|// TODO: fails see CAMEL-583
comment|/*         MessageType message = converter.convertTo(MessageType.class, "<message><hello>bar</hello></message>");          assertNotNull("Message should not be null!", message);         */
block|}
block|}
end_class

end_unit

