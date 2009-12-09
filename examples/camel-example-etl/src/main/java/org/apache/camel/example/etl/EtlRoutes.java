begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.etl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|etl
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|spring
operator|.
name|SpringRouteBuilder
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
name|LoggingLevel
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
name|processor
operator|.
name|interceptor
operator|.
name|Tracer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|juel
operator|.
name|JuelExpression
operator|.
name|el
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_comment
comment|// START SNIPPET: example
end_comment

begin_class
DECL|class|EtlRoutes
specifier|public
class|class
name|EtlRoutes
extends|extends
name|SpringRouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|/** 	Tracer tracer = new Tracer();         tracer.setLogLevel(LoggingLevel.FATAL);         tracer.setLogName("org.apache.camel.example.etl");         tracer.setLogStackTrace(true);         tracer.setTraceExceptions(true);         **/
name|from
argument_list|(
literal|"file:src/data?noop=true"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|PersonDocument
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa:org.apache.camel.example.etl.CustomerEntity"
argument_list|)
expr_stmt|;
comment|// the following will dump the database to files
name|from
argument_list|(
literal|"jpa:org.apache.camel.example.etl.CustomerEntity?consumeDelete=false&delay=3000&consumeLockEntity=false"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|el
argument_list|(
literal|"${in.body.userName}.xml"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/customers"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

