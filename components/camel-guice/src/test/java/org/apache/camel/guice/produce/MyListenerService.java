begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.produce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|produce
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
name|Consume
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MyListenerService
specifier|public
class|class
name|MyListenerService
implements|implements
name|MyListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MyListenerService
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|MyListenerService ()
specifier|public
name|MyListenerService
parameter_list|()
block|{     }
annotation|@
name|Consume
argument_list|(
name|uri
operator|=
literal|"direct:myService"
argument_list|)
DECL|method|sayHello (String name)
specifier|public
name|String
name|sayHello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Invoked sayHello with: "
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
block|}
end_class

end_unit

