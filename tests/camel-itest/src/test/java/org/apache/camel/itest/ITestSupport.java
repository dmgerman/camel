begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
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
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_comment
comment|/**  * For test cases that use unique contexts, they can share the   * ports which will make things a bit faster as ports aren't opened  * and closed all the time.   */
end_comment

begin_class
DECL|class|ITestSupport
specifier|public
specifier|final
class|class
name|ITestSupport
block|{
DECL|field|PORT1
specifier|static
specifier|final
name|int
name|PORT1
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|PORT2
specifier|static
specifier|final
name|int
name|PORT2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|PORT3
specifier|static
specifier|final
name|int
name|PORT3
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|PORT4
specifier|static
specifier|final
name|int
name|PORT4
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
static|static
block|{
comment|//set them as system properties so Spring can use the property placeholder
comment|//things to set them into the URL's in the spring contexts
name|System
operator|.
name|setProperty
argument_list|(
literal|"ITestSupport.port1"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|PORT1
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"ITestSupport.port2"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|PORT2
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"ITestSupport.port3"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|PORT3
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"ITestSupport.port4"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|PORT4
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ITestSupport ()
specifier|private
name|ITestSupport
parameter_list|()
block|{     }
DECL|method|getPort (String name)
specifier|public
specifier|static
name|int
name|getPort
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|port
return|;
block|}
DECL|method|getPort1 ()
specifier|public
specifier|static
name|int
name|getPort1
parameter_list|()
block|{
return|return
name|PORT1
return|;
block|}
DECL|method|getPort2 ()
specifier|public
specifier|static
name|int
name|getPort2
parameter_list|()
block|{
return|return
name|PORT2
return|;
block|}
DECL|method|getPort3 ()
specifier|public
specifier|static
name|int
name|getPort3
parameter_list|()
block|{
return|return
name|PORT3
return|;
block|}
block|}
end_class

end_unit

