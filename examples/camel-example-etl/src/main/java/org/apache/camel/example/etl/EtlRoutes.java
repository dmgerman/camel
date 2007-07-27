begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Processor
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
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|EtlRoutes
specifier|public
class|class
name|EtlRoutes
extends|extends
name|RouteBuilder
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|EtlRoutes
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
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
comment|/*         from("file:src/data?noop=true").convertBodyTo(PersonDocument.class).process(new Processor() {             public void process(Exchange exchange) throws Exception {                 System.out.println("Body is: " + exchange.getIn().getBody());                 log.info("Body is: " + exchange.getIn().getBody());             }         }); */
block|}
block|}
end_class

end_unit

