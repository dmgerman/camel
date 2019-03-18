begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|XmlConfigBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|Hazelcast
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_class
DECL|class|HazelcastUtil
specifier|public
specifier|final
class|class
name|HazelcastUtil
block|{
DECL|method|HazelcastUtil ()
specifier|private
name|HazelcastUtil
parameter_list|()
block|{     }
DECL|method|newInstance ()
specifier|public
specifier|static
name|HazelcastInstance
name|newInstance
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|XmlConfigBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// hazelcast.version.check.enabled is deprecated
name|cfg
operator|.
name|setProperty
argument_list|(
literal|"hazelcast.phone.home.enabled"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"hazelcast.phone.home.enabled"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setProperty
argument_list|(
literal|"hazelcast.logging.type"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"hazelcast.logging.type"
argument_list|,
literal|"slf4j"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|newInstance
argument_list|(
name|cfg
argument_list|)
return|;
block|}
DECL|method|newInstance (Config cfg)
specifier|public
specifier|static
name|HazelcastInstance
name|newInstance
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
return|return
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
name|cfg
argument_list|)
return|;
block|}
block|}
end_class

end_unit

