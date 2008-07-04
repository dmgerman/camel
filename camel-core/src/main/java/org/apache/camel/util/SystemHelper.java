begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|converter
operator|.
name|ObjectConverter
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
comment|/**  * A helper class for various {@link System} related methods  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SystemHelper
specifier|public
specifier|final
class|class
name|SystemHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|SystemHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|SystemHelper ()
specifier|private
name|SystemHelper
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * Looks up the given system property name returning null if any exceptions occur      */
DECL|method|getSystemProperty (String name)
specifier|public
specifier|static
name|String
name|getSystemProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught exception looking for system property: "
operator|+
name|name
operator|+
literal|" exception: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Looks up the given system property boolean value. Returns false if the system property doesn't exist.      */
DECL|method|isSystemProperty (String name)
specifier|public
specifier|static
name|boolean
name|isSystemProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|text
init|=
name|getSystemProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|ObjectConverter
operator|.
name|toBool
argument_list|(
name|text
argument_list|)
return|;
block|}
block|}
end_class

end_unit

