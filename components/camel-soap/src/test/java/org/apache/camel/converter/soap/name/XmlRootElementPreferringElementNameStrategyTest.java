begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|annotation
operator|.
name|XmlRootElement
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
name|annotation
operator|.
name|XmlType
import|;
end_import

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
operator|.
name|testpackage
operator|.
name|RequestWithDefaultNs
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
name|XmlRootElementPreferringElementNameStrategy
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|XmlRootElementPreferringElementNameStrategyTest
specifier|public
class|class
name|XmlRootElementPreferringElementNameStrategyTest
block|{
DECL|field|DEFAULT_NS
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_NS
init|=
literal|"##default"
decl_stmt|;
DECL|field|CUSTOM_NS
specifier|private
specifier|static
specifier|final
name|String
name|CUSTOM_NS
init|=
literal|"http://test.com/sample"
decl_stmt|;
DECL|field|LOCAL_NAME
specifier|private
specifier|static
specifier|final
name|String
name|LOCAL_NAME
init|=
literal|"sample"
decl_stmt|;
DECL|field|ens
specifier|private
name|XmlRootElementPreferringElementNameStrategy
name|ens
init|=
operator|new
name|XmlRootElementPreferringElementNameStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testFindQNameForSoapActionOrTypeWithXmlSchemaPresent ()
specifier|public
name|void
name|testFindQNameForSoapActionOrTypeWithXmlSchemaPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|qname
init|=
name|ens
operator|.
name|findQNameForSoapActionOrType
argument_list|(
literal|"abc"
argument_list|,
name|RequestWithDefaultNs
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"local names must match"
argument_list|,
literal|"foo"
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"namespace must match"
argument_list|,
literal|"baz"
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFindQNameForSoapActionOrType ()
specifier|public
name|void
name|testFindQNameForSoapActionOrType
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|qname
init|=
name|ens
operator|.
name|findQNameForSoapActionOrType
argument_list|(
name|DEFAULT_NS
argument_list|,
name|Request
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"local names must match"
argument_list|,
name|LOCAL_NAME
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"namespace must match"
argument_list|,
name|CUSTOM_NS
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|qname
operator|=
name|ens
operator|.
name|findQNameForSoapActionOrType
argument_list|(
name|CUSTOM_NS
argument_list|,
name|Request
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"local names must match"
argument_list|,
name|LOCAL_NAME
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"namespace must match"
argument_list|,
name|CUSTOM_NS
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testFindExceptionForFaultName ()
specifier|public
name|void
name|testFindExceptionForFaultName
parameter_list|()
throws|throws
name|Exception
block|{
name|ens
operator|.
name|findExceptionForFaultName
argument_list|(
operator|new
name|QName
argument_list|(
name|LOCAL_NAME
argument_list|,
name|CUSTOM_NS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|""
argument_list|,
name|propOrder
operator|=
block|{
name|LOCAL_NAME
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
name|LOCAL_NAME
argument_list|,
name|namespace
operator|=
name|CUSTOM_NS
argument_list|)
DECL|class|Request
specifier|public
class|class
name|Request
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
block|}
end_class

end_unit

