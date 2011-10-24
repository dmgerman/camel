begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.krati
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|krati
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|store
operator|.
name|DataStore
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

begin_class
DECL|class|KratiDataStoreRegistration
specifier|public
class|class
name|KratiDataStoreRegistration
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KratiDataStoreRegistration
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dataStore
specifier|private
specifier|final
name|DataStore
name|dataStore
decl_stmt|;
DECL|field|registrationCount
specifier|private
name|int
name|registrationCount
decl_stmt|;
DECL|method|KratiDataStoreRegistration (DataStore dataStore)
specifier|public
name|KratiDataStoreRegistration
parameter_list|(
name|DataStore
name|dataStore
parameter_list|)
block|{
name|this
operator|.
name|dataStore
operator|=
name|dataStore
expr_stmt|;
block|}
DECL|method|register ()
specifier|public
name|int
name|register
parameter_list|()
block|{
return|return
operator|++
name|registrationCount
return|;
block|}
DECL|method|unregister ()
specifier|public
name|boolean
name|unregister
parameter_list|()
block|{
if|if
condition|(
operator|--
name|registrationCount
operator|<=
literal|0
condition|)
block|{
try|try
block|{
name|dataStore
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error while closing datastore."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|getDataStore ()
specifier|public
name|DataStore
name|getDataStore
parameter_list|()
block|{
name|register
argument_list|()
expr_stmt|;
return|return
name|dataStore
return|;
block|}
block|}
end_class

end_unit

