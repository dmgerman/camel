begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.firebase.exception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|firebase
operator|.
name|exception
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|firebase
operator|.
name|database
operator|.
name|DatabaseError
import|;
end_import

begin_comment
comment|/**  * In case Firebase throws a database error, this object wraps a Throwable around the original object.  */
end_comment

begin_class
DECL|class|DatabaseErrorException
specifier|public
class|class
name|DatabaseErrorException
extends|extends
name|RuntimeException
block|{
DECL|field|databaseError
specifier|private
specifier|final
name|DatabaseError
name|databaseError
decl_stmt|;
DECL|method|DatabaseErrorException (DatabaseError databaseError)
specifier|public
name|DatabaseErrorException
parameter_list|(
name|DatabaseError
name|databaseError
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|databaseError
operator|=
name|databaseError
expr_stmt|;
block|}
block|}
end_class

end_unit

