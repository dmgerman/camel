begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanstalk.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanstalk
operator|.
name|integration
package|;
end_package

begin_import
import|import
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|Client
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
name|beanstalk
operator|.
name|ConnectionSettings
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
name|beanstalk
operator|.
name|ConnectionSettingsFactory
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

begin_class
DECL|class|BeanstalkCamelTestSupport
specifier|public
specifier|abstract
class|class
name|BeanstalkCamelTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|connFactory
specifier|protected
specifier|final
name|ConnectionSettingsFactory
name|connFactory
init|=
name|ConnectionSettingsFactory
operator|.
name|DEFAULT
decl_stmt|;
DECL|field|tubeName
specifier|protected
specifier|final
name|String
name|tubeName
init|=
name|String
operator|.
name|format
argument_list|(
literal|"test%d"
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|reader
specifier|protected
name|Client
name|reader
decl_stmt|;
DECL|field|writer
specifier|protected
name|Client
name|writer
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
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
name|ConnectionSettings
name|conn
init|=
name|connFactory
operator|.
name|parseUri
argument_list|(
name|tubeName
argument_list|)
decl_stmt|;
name|writer
operator|=
name|conn
operator|.
name|newWritingClient
argument_list|()
expr_stmt|;
name|reader
operator|=
name|conn
operator|.
name|newReadingClient
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

