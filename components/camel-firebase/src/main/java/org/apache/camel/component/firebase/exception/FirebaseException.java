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

begin_comment
comment|/**  * Used to mark an exception occurred in the Firebase Camel processor.  */
end_comment

begin_class
DECL|class|FirebaseException
specifier|public
class|class
name|FirebaseException
extends|extends
name|RuntimeException
block|{
comment|/**      * Constructs a new runtime exception with the specified detail message and      * cause.<p>Note that the detail message associated with      * {@code cause} is<i>not</i> automatically incorporated in      * this runtime exception's detail message.      *      * @param message the detail message (which is saved for later retrieval      *                by the {@link #getMessage()} method).      * @param cause   the cause (which is saved for later retrieval by the      *                {@link #getCause()} method).  (A<tt>null</tt> value is      *                permitted, and indicates that the cause is nonexistent or      *                unknown.)      * @since 1.4      */
DECL|method|FirebaseException (String message, Throwable cause)
specifier|public
name|FirebaseException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

