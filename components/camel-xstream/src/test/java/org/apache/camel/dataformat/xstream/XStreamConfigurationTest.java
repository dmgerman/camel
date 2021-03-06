begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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
name|List
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
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|Converter
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
name|converters
operator|.
name|MarshallingContext
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
name|converters
operator|.
name|UnmarshallingContext
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
name|HierarchicalStreamReader
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
name|HierarchicalStreamWriter
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|model
operator|.
name|dataformat
operator|.
name|XStreamDataFormat
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

begin_comment
comment|/**  * Marshal tests with domain objects.  */
end_comment

begin_class
DECL|class|XStreamConfigurationTest
specifier|public
class|class
name|XStreamConfigurationTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|constructorInjected
specifier|private
specifier|static
specifier|volatile
name|boolean
name|constructorInjected
decl_stmt|;
DECL|field|methodInjected
specifier|private
specifier|static
specifier|volatile
name|boolean
name|methodInjected
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|constructorInjected
operator|=
literal|false
expr_stmt|;
name|methodInjected
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|testXStreamInjection ()
specifier|public
name|void
name|testXStreamInjection
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|constructorInjected
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|methodInjected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomMarshalDomainObject ()
specifier|public
name|void
name|testCustomMarshalDomainObject
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setName
argument_list|(
literal|"Tiger"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|order
operator|.
name|setPrice
argument_list|(
literal|99.95
argument_list|)
expr_stmt|;
name|String
name|ordereString
init|=
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<purchase-order name=\"Tiger\" price=\"99.95\" amount=\"1.0\"/>"
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
operator|new
name|Object
index|[]
block|{
name|ordereString
block|,
name|order
block|}
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|ordereString
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomMarshalDomainObjectWithImplicit ()
specifier|public
name|void
name|testCustomMarshalDomainObjectWithImplicit
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|PurchaseHistory
name|history
init|=
operator|new
name|PurchaseHistory
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Double
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|11.5
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|97.5
argument_list|)
expr_stmt|;
name|history
operator|.
name|setHistory
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|String
name|ordereString
init|=
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<org.apache.camel.dataformat.xstream.PurchaseHistory>"
operator|+
literal|"<double>11.5</double><double>97.5</double>"
operator|+
literal|"</org.apache.camel.dataformat.xstream.PurchaseHistory>"
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
operator|new
name|Object
index|[]
block|{
name|ordereString
block|,
name|history
block|}
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|history
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|ordereString
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomMarshalDomainObjectJson ()
specifier|public
name|void
name|testCustomMarshalDomainObjectJson
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setName
argument_list|(
literal|"Tiger"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|order
operator|.
name|setPrice
argument_list|(
literal|99.95
argument_list|)
expr_stmt|;
name|String
name|ordereString
init|=
literal|"{\"purchase-order\":{\"@name\":\"Tiger\",\"@price\":99.95,\"@amount\":1}}"
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
operator|new
name|Object
index|[]
block|{
name|ordereString
block|,
name|order
block|}
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal-json"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal-json"
argument_list|,
name|ordereString
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomXStreamDriverMarshal ()
specifier|public
name|void
name|testCustomXStreamDriverMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setName
argument_list|(
literal|"Tiger"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|order
operator|.
name|setPrice
argument_list|(
literal|99.95
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:myDriver"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|String
name|result
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// make sure the result is start with "{"
name|assertTrue
argument_list|(
literal|"Should get a json result"
argument_list|,
name|result
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|XStreamDataFormat
name|xstreamDefinition
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
argument_list|>
name|aliases
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|aliases
operator|.
name|put
argument_list|(
literal|"purchase-order"
argument_list|,
name|PurchaseOrder
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|xstreamDefinition
operator|.
name|setAliases
argument_list|(
name|aliases
argument_list|)
expr_stmt|;
name|xstreamDefinition
operator|.
name|setPermissions
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|,
name|PurchaseHistory
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|converters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|converters
operator|.
name|add
argument_list|(
name|PurchaseOrderConverter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|converters
operator|.
name|add
argument_list|(
name|CheckMethodInjection
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|converters
operator|.
name|add
argument_list|(
name|CheckConstructorInjection
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|xstreamDefinition
operator|.
name|setConverters
argument_list|(
name|converters
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|implicits
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|implicits
operator|.
name|put
argument_list|(
name|PurchaseHistory
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
literal|"history"
block|}
argument_list|)
expr_stmt|;
name|xstreamDefinition
operator|.
name|setImplicitCollections
argument_list|(
name|implicits
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xstreamDefinition
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xstreamDefinition
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|xstreamDefinition
operator|=
operator|new
name|XStreamDataFormat
argument_list|()
expr_stmt|;
name|xstreamDefinition
operator|.
name|setDriver
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
name|aliases
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|aliases
operator|.
name|put
argument_list|(
literal|"purchase-order"
argument_list|,
name|PurchaseOrder
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|xstreamDefinition
operator|.
name|setAliases
argument_list|(
name|aliases
argument_list|)
expr_stmt|;
name|xstreamDefinition
operator|.
name|setPermissions
argument_list|(
name|PurchaseOrder
operator|.
name|class
argument_list|,
name|PurchaseHistory
operator|.
name|class
argument_list|)
expr_stmt|;
name|converters
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|converters
operator|.
name|add
argument_list|(
name|PurchaseOrderConverter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|xstreamDefinition
operator|.
name|setConverters
argument_list|(
name|converters
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshal-json"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xstreamDefinition
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshal-json"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xstreamDefinition
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xstream
operator|.
name|XStreamDataFormat
name|xStreamDataFormat
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xstream
operator|.
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
name|xStreamDataFormat
operator|.
name|setPermissions
argument_list|(
literal|"+6org.apache.camel.dataformat.xstream.*"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:myDriver"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xStreamDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|PurchaseOrderConverter
specifier|public
specifier|static
class|class
name|PurchaseOrderConverter
implements|implements
name|Converter
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|canConvert (Class type)
specifier|public
name|boolean
name|canConvert
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
name|PurchaseOrder
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (HierarchicalStreamReader reader, UnmarshallingContext context)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|HierarchicalStreamReader
name|reader
parameter_list|,
name|UnmarshallingContext
name|context
parameter_list|)
block|{
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setName
argument_list|(
name|reader
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|order
operator|.
name|setPrice
argument_list|(
name|Double
operator|.
name|parseDouble
argument_list|(
name|reader
operator|.
name|getAttribute
argument_list|(
literal|"price"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
name|Double
operator|.
name|parseDouble
argument_list|(
name|reader
operator|.
name|getAttribute
argument_list|(
literal|"amount"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|order
return|;
block|}
annotation|@
name|Override
DECL|method|marshal (Object object, HierarchicalStreamWriter writer, MarshallingContext context)
specifier|public
name|void
name|marshal
parameter_list|(
name|Object
name|object
parameter_list|,
name|HierarchicalStreamWriter
name|writer
parameter_list|,
name|MarshallingContext
name|context
parameter_list|)
block|{
name|writer
operator|.
name|addAttribute
argument_list|(
literal|"name"
argument_list|,
operator|(
operator|(
name|PurchaseOrder
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addAttribute
argument_list|(
literal|"price"
argument_list|,
name|Double
operator|.
name|toString
argument_list|(
operator|(
operator|(
name|PurchaseOrder
operator|)
name|object
operator|)
operator|.
name|getPrice
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|addAttribute
argument_list|(
literal|"amount"
argument_list|,
name|Double
operator|.
name|toString
argument_list|(
operator|(
operator|(
name|PurchaseOrder
operator|)
name|object
operator|)
operator|.
name|getAmount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|CheckConstructorInjection
specifier|public
specifier|static
class|class
name|CheckConstructorInjection
implements|implements
name|Converter
block|{
DECL|method|CheckConstructorInjection (XStream xstream)
specifier|public
name|CheckConstructorInjection
parameter_list|(
name|XStream
name|xstream
parameter_list|)
block|{
if|if
condition|(
name|xstream
operator|!=
literal|null
condition|)
block|{
name|constructorInjected
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"XStream should not be null"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|marshal (Object source, HierarchicalStreamWriter writer, MarshallingContext context)
specifier|public
name|void
name|marshal
parameter_list|(
name|Object
name|source
parameter_list|,
name|HierarchicalStreamWriter
name|writer
parameter_list|,
name|MarshallingContext
name|context
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|unmarshal (HierarchicalStreamReader reader, UnmarshallingContext context)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|HierarchicalStreamReader
name|reader
parameter_list|,
name|UnmarshallingContext
name|context
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|canConvert (Class type)
specifier|public
name|boolean
name|canConvert
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|class|CheckMethodInjection
specifier|public
specifier|static
class|class
name|CheckMethodInjection
implements|implements
name|Converter
block|{
DECL|method|CheckMethodInjection ()
specifier|public
name|CheckMethodInjection
parameter_list|()
block|{          }
DECL|method|setXStream (XStream xstream)
specifier|public
name|void
name|setXStream
parameter_list|(
name|XStream
name|xstream
parameter_list|)
block|{
if|if
condition|(
name|xstream
operator|!=
literal|null
condition|)
block|{
name|methodInjected
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"XStream should not be null"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|marshal (Object source, HierarchicalStreamWriter writer, MarshallingContext context)
specifier|public
name|void
name|marshal
parameter_list|(
name|Object
name|source
parameter_list|,
name|HierarchicalStreamWriter
name|writer
parameter_list|,
name|MarshallingContext
name|context
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|unmarshal (HierarchicalStreamReader reader, UnmarshallingContext context)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|HierarchicalStreamReader
name|reader
parameter_list|,
name|UnmarshallingContext
name|context
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|canConvert (Class type)
specifier|public
name|boolean
name|canConvert
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

