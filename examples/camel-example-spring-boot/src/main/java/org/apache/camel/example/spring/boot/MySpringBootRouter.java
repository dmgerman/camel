begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|spring
operator|.
name|boot
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
name|spring
operator|.
name|boot
operator|.
name|FatJarRouter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|HealthEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_class
annotation|@
name|SpringBootApplication
DECL|class|MySpringBootRouter
specifier|public
class|class
name|MySpringBootRouter
extends|extends
name|FatJarRouter
block|{
annotation|@
name|Autowired
DECL|field|health
specifier|private
name|HealthEndpoint
name|health
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"timer:trigger"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"ref:myBean"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:out"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:status"
argument_list|)
operator|.
name|bean
argument_list|(
name|health
argument_list|,
literal|"invoke"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Health is ${body}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Bean
DECL|method|myBean ()
name|String
name|myBean
parameter_list|()
block|{
return|return
literal|"I'm Spring bean!"
return|;
block|}
block|}
end_class

end_unit

