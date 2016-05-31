begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|WriteConcern
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|WriteResult
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
name|junit
operator|.
name|Ignore
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
DECL|class|MongoDbWriteConcernsTest
specifier|public
class|class
name|MongoDbWriteConcernsTest
extends|extends
name|AbstractMongoDbTest
block|{
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testDynamicWriteConcernSafe ()
specifier|public
name|void
name|testDynamicWriteConcernSafe
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// test with object first
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:noWriteConcern"
argument_list|,
literal|"{\"scientist\":\"newton\"}"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type WriteResult"
argument_list|,
name|result
operator|instanceof
name|WriteResult
argument_list|)
expr_stmt|;
name|WriteResult
name|wr
init|=
operator|(
name|WriteResult
operator|)
name|result
decl_stmt|;
name|assertTrue
argument_list|(
name|wr
operator|.
name|wasAcknowledged
argument_list|()
argument_list|)
expr_stmt|;
comment|// same behaviour should be reproduced with String 'SAFE'
name|result
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:noWriteConcern"
argument_list|,
literal|"{\"scientist\":\"newton\"}"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Result is not of type WriteResult"
argument_list|,
name|result
operator|instanceof
name|WriteResult
argument_list|)
expr_stmt|;
name|wr
operator|=
operator|(
name|WriteResult
operator|)
name|result
expr_stmt|;
name|assertTrue
argument_list|(
name|wr
operator|.
name|wasAcknowledged
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testDynamicWriteConcernUnknown ()
specifier|public
name|void
name|testDynamicWriteConcernUnknown
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testCollection
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:noWriteConcern"
argument_list|,
literal|"{\"scientist\":\"newton\"}"
argument_list|,
name|MongoDbConstants
operator|.
name|WRITECONCERN
argument_list|,
literal|"Random"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|extractAndAssertCamelMongoDbException
argument_list|(
name|e
argument_list|,
literal|"WriteConcern specified in the "
operator|+
name|MongoDbConstants
operator|.
name|WRITECONCERN
operator|+
literal|" header"
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:noWriteConcern"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database=test&collection=camelTest&operation=insert"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:writeConcernParam"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database=test&collection=camelTest&operation=insert&writeConcern=SAFE"
argument_list|)
expr_stmt|;
comment|//from("direct:writeConcernRef").to("mongodb:myDb?database=test&collection=camelTest&operation=insert&writeConcernRef=customWriteConcern");
name|from
argument_list|(
literal|"direct:noWriteConcernWithCallGetLastError"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mongodb:myDb?database=test&collection=camelTest&operation=insert"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

