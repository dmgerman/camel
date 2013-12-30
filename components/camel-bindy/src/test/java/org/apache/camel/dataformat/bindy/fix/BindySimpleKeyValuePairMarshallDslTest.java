begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|fix
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
name|Calendar
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
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|simple
operator|.
name|Order
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
name|BindyType
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
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|BindySimpleKeyValuePairMarshallDslTest
specifier|public
class|class
name|BindySimpleKeyValuePairMarshallDslTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|models
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|result
specifier|private
name|String
name|result
init|=
literal|"1=BE.CHM.001
literal|11=CHM0001-01
literal|22=4
literal|48=BE0001245678"
operator|+
literal|"
literal|54=1
literal|58=this is a camel - bindy test
literal|777=17-02-2011 23:29:59
literal|\r\n"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshallMessage ()
specifier|public
name|void
name|testMarshallMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|generateModel
argument_list|()
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|generateModel ()
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|generateModel
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|modelObjects
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Order
name|order
init|=
operator|new
name|Order
argument_list|()
decl_stmt|;
name|order
operator|.
name|setAccount
argument_list|(
literal|"BE.CHM.001"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setClOrdId
argument_list|(
literal|"CHM0001-01"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setIDSource
argument_list|(
literal|"4"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setSecurityId
argument_list|(
literal|"BE0001245678"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setSide
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setText
argument_list|(
literal|"this is a camel - bindy test"
argument_list|)
expr_stmt|;
name|Calendar
name|calendar
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT"
argument_list|)
argument_list|)
decl_stmt|;
comment|// 4 hour shift
comment|// 17-02-2011 23:29:59 by GMT-3
name|calendar
operator|.
name|set
argument_list|(
literal|2011
argument_list|,
literal|1
argument_list|,
literal|18
argument_list|,
literal|2
argument_list|,
literal|29
argument_list|,
literal|59
argument_list|)
expr_stmt|;
name|order
operator|.
name|setCreated
argument_list|(
name|calendar
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|modelObjects
operator|.
name|put
argument_list|(
name|order
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|modelObjects
argument_list|)
expr_stmt|;
return|return
name|models
return|;
block|}
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|bindy
argument_list|(
name|BindyType
operator|.
name|KeyValue
argument_list|,
literal|"org.apache.camel.dataformat.bindy.model.fix.simple"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

