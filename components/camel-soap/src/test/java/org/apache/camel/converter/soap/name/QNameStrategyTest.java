begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.soap.name
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|soap
operator|.
name|name
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|com
operator|.
name|example
operator|.
name|customerservice
operator|.
name|GetCustomersByName
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
name|dataformat
operator|.
name|soap
operator|.
name|name
operator|.
name|QNameStrategy
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
comment|/**  *   */
end_comment

begin_class
DECL|class|QNameStrategyTest
specifier|public
class|class
name|QNameStrategyTest
block|{
comment|/**      * The strategy should always produce the QName that      * it was given on the constructor      */
annotation|@
name|Test
DECL|method|testQName ()
specifier|public
name|void
name|testQName
parameter_list|()
block|{
name|QName
name|elementName
init|=
operator|new
name|QName
argument_list|(
literal|"http://my.name.org"
argument_list|,
literal|"myElement"
argument_list|)
decl_stmt|;
name|QNameStrategy
name|strategy
init|=
operator|new
name|QNameStrategy
argument_list|(
name|elementName
argument_list|)
decl_stmt|;
name|QName
name|actualElementName
init|=
name|strategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|null
argument_list|,
name|GetCustomersByName
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|elementName
argument_list|,
name|actualElementName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

