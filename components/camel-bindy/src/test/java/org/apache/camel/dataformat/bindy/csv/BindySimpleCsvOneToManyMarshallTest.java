begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
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
name|csv
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
name|simple
operator|.
name|onetomany
operator|.
name|Author
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
name|simple
operator|.
name|onetomany
operator|.
name|Book
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
DECL|class|BindySimpleCsvOneToManyMarshallTest
specifier|public
class|class
name|BindySimpleCsvOneToManyMarshallTest
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
literal|"Charles,Moulliard,Camel in Action 1,2010,43\r\n"
operator|+
literal|"Charles,Moulliard,Camel in Action 2,2012,43\r\n"
operator|+
literal|"Charles,Moulliard,Camel in Action 3,2013,43\r\n"
operator|+
literal|"Charles,Moulliard,Camel in Action 4,,43\r\n"
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
name|Author
name|author
decl_stmt|;
name|Book
name|book
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
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
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
operator|new
name|ArrayList
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
comment|// List<Reference> references = new ArrayList<Reference>();
comment|// List<Editor> editors = new ArrayList<Editor>();
name|author
operator|=
operator|new
name|Author
argument_list|()
expr_stmt|;
name|author
operator|.
name|setFirstName
argument_list|(
literal|"Charles"
argument_list|)
expr_stmt|;
name|author
operator|.
name|setLastName
argument_list|(
literal|"Moulliard"
argument_list|)
expr_stmt|;
name|author
operator|.
name|setAge
argument_list|(
literal|"43"
argument_list|)
expr_stmt|;
comment|// 1st Book
name|book
operator|=
operator|new
name|Book
argument_list|()
expr_stmt|;
name|book
operator|.
name|setTitle
argument_list|(
literal|"Camel in Action 1"
argument_list|)
expr_stmt|;
name|book
operator|.
name|setYear
argument_list|(
literal|"2010"
argument_list|)
expr_stmt|;
name|books
operator|.
name|add
argument_list|(
name|book
argument_list|)
expr_stmt|;
comment|// 2nd book
name|book
operator|=
operator|new
name|Book
argument_list|()
expr_stmt|;
name|book
operator|.
name|setTitle
argument_list|(
literal|"Camel in Action 2"
argument_list|)
expr_stmt|;
name|book
operator|.
name|setYear
argument_list|(
literal|"2012"
argument_list|)
expr_stmt|;
name|books
operator|.
name|add
argument_list|(
name|book
argument_list|)
expr_stmt|;
comment|// 3rd book
name|book
operator|=
operator|new
name|Book
argument_list|()
expr_stmt|;
name|book
operator|.
name|setTitle
argument_list|(
literal|"Camel in Action 3"
argument_list|)
expr_stmt|;
name|book
operator|.
name|setYear
argument_list|(
literal|"2013"
argument_list|)
expr_stmt|;
name|books
operator|.
name|add
argument_list|(
name|book
argument_list|)
expr_stmt|;
comment|// 4th book
name|book
operator|=
operator|new
name|Book
argument_list|()
expr_stmt|;
name|book
operator|.
name|setTitle
argument_list|(
literal|"Camel in Action 4"
argument_list|)
expr_stmt|;
name|book
operator|.
name|setYear
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|books
operator|.
name|add
argument_list|(
name|book
argument_list|)
expr_stmt|;
comment|// Add books to author
name|author
operator|.
name|setBooks
argument_list|(
name|books
argument_list|)
expr_stmt|;
name|model
operator|.
name|put
argument_list|(
name|author
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|author
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|model
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
DECL|field|camelDataFormat
name|BindyCsvDataFormat
name|camelDataFormat
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
literal|"org.apache.camel.dataformat.bindy.model.simple.onetomany"
argument_list|)
decl_stmt|;
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
argument_list|(
name|camelDataFormat
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

